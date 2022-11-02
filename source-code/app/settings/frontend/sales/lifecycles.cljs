
(ns app.settings.frontend.sales.lifecycles
    (:require [app.home.frontend.api]
              [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:home.screen/add-menu-item! {:group       :settings
                                              :icon        :payments
                                              :icon-color  "#5f8e75"
                                              :icon-family :material-icons-outlined
                                              :label       :sales
                                              :on-click    [:router/go-to! "/@app-home/settings/sales"]
                                              :horizontal-weight 4}]})
