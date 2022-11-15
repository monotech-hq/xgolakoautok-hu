
(ns app.settings.frontend.appearance.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:home.screen/add-menu-item! {:disabled?   true
                                              :group-name  :settings
                                              :icon        :auto_awesome
                                              :icon-color  "#ab55b1"
                                              :icon-family :material-icons-outlined
                                              :label       :appearance
                                              :on-click    [:x.router/go-to! "/@app-home/settings/appearance"]
                                              :horizontal-weight 1}]})
