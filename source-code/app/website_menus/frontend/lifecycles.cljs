
(ns app.website-menus.frontend.lifecycles
    (:require [app.website-menus.frontend.dictionary :as dictionary]
              [x.core.api                            :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:x.dictionary/add-terms! dictionary/BOOK]})
