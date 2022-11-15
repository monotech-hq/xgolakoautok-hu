
(ns app.settings.frontend.sales.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:home.screen/add-menu-item! {:group-name  :settings
                                              :icon        :payments
                                              :icon-color  "#5f8e75"
                                              :icon-family :material-icons-outlined
                                              :label       :sales
                                              :on-click    [:x.router/go-to! "/@app-home/settings/sales"]
                                              :horizontal-weight 4}]})
