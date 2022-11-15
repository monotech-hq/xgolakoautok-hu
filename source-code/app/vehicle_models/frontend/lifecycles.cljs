
(ns app.vehicle-models.frontend.lifecycles
    (:require [app.vehicle-models.frontend.dictionary :as dictionary]
              [x.core.api                             :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:x.dictionary/add-terms! dictionary/BOOK]})
