
(ns app.packages.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group      :products-and-services
                                                            :icon       :workspaces_filled
                                                            :icon-color "#449997"
                                                            :label      :packages
                                                            :on-click   [:router/go-to! "/@app-home/packages"]
                                                            :horizontal-weight 2}]
                              [:home.sidebar/add-menu-item! {:group      :products-and-services
                                                             :icon       :workspaces_filled
                                                             :icon-color "#449997"
                                                             :label      :packages
                                                             :on-click   [:router/go-to! "/@app-home/packages"]
                                                             :vertical-weight 2}]]}})
