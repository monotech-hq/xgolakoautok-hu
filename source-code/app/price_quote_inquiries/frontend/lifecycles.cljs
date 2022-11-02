
(ns app.price-quote-inquiries.frontend.lifecycles
    (:require [app.price-quote-inquiries.frontend.dictionary :as dictionary]
              [x.app-core.api                                :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:dictionary/add-terms! dictionary/BOOK]})
