
(ns app.home.frontend.lifecycles
    (:require [app.home.frontend.dictionary :as dictionary]
              [x.core.api                   :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:x.dictionary/add-terms! dictionary/BOOK]})
