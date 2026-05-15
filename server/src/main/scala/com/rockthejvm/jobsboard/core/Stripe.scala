package com.rockthejvm.jobsboard.core

import cats.*
import cats.implicits.*
import org.typelevel.log4cats.Logger

import com.stripe.{Stripe => TheStripe}
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams

import com.rockthejvm.jobsboard.logging.syntax.*
import com.rockthejvm.jobsboard.config.*
/* 
   secret key:
    sk_test_51TXQQRCmWFBchYXhMWN5HsfpMDCyRWSGIvNCkYW4knV80EfI70foStaw7PWXKuxxcWUoxamWXDEP6jJdMLl2AMmm00Yh3j5jP2

    product id:
    prod_UWTUYCfPYO5U6b

 */
trait Stripe[F[_]] {
    def createCheckoutSession(jobId: String, userEmail: String): F[Option[Session]]
}


class LiveStripe[F[_]: MonadThrow : Logger](
    key: String,
    price: String,
    successUrl: String,
    cancelUrl: String
) extends Stripe[F] {
    TheStripe.apiKey = key 
    def createCheckoutSession(jobId: String, userEmail: String): F[Option[Session]] =
        SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setInvoiceCreation(
            SessionCreateParams.InvoiceCreation.builder().setEnabled(true).build()
        )
        .setPaymentIntentData(
            SessionCreateParams.PaymentIntentData.builder().setReceiptEmail(userEmail).build()
        )
        .setSuccessUrl(s"$successUrl/$jobId")
        .setCancelUrl(cancelUrl)
        .setCustomerEmail(userEmail)
        .setClientReferenceId(jobId)
        .addLineItem(
           SessionCreateParams.LineItem.builder()
              .setQuantity(1L)
              // Provide the exact Price ID (for example, price_1234) of the product you want to sell
              .setPrice(price)
              .build())
        .build()
        .pure[F]
        .map(params => Session.create(params))
        .map(_.some)
        .logError(error => s"Creating checkout session failed: $error")
        .recover {case _ => None}
}


object LiveStripe {
    def apply[F[_]: MonadThrow : Logger](stripeConfig: StripeConfig): F[LiveStripe[F]] =
        new LiveStripe[F](
            stripeConfig.key,
            stripeConfig.price,
            stripeConfig.successUrl,
            stripeConfig.cancelUrl
        ).pure[F]
}