
(ns app.website-menus.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name :website
                                                            :icon       :menu
                                                            :icon-color "#8655b1"
                                                            :label      :website-menus
                                                            :on-click   [:x.router/go-to! "/@app-home/website-menus"]
                                                            :horizontal-weight 2}]
                              [:home.sidebar/add-menu-item! {:group-name :website
                                                             :icon       :menu
                                                             :icon-color "#8655b1"
                                                             :label      :website-menus
                                                             :on-click   [:x.router/go-to! "/@app-home/website-menus"]
                                                             :vertical-weight 2}]]}})
