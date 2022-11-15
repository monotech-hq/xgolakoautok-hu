
(ns app.price-quote-inquiries.frontend.lifecycles
    (:require [app.price-quote-inquiries.frontend.dictionary :as dictionary]
              [x.core.api                                    :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:x.dictionary/add-terms! dictionary/BOOK]})
