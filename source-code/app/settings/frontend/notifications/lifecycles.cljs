
(ns app.settings.frontend.notifications.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:home.screen/add-menu-item! {:disabled?   true
                                              :group-name  :settings
                                              :icon        :notifications
                                              :icon-color  "#8eb155"
                                              :icon-family :material-icons-outlined
                                              :label       :notifications
                                              :on-click    [:x.router/go-to! "/@app-home/settings/notifications"]
                                              :horizontal-weight 2}]})
