
(ns app.rental-vehicles.frontend.lifecycles
    (:require [app.rental-vehicles.frontend.dictionary :as dictionary]
              [x.core.api                              :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:x.dictionary/add-terms! dictionary/BOOK]})
