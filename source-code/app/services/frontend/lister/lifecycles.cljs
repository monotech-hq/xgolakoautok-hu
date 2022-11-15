
(ns app.services.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group      :products-and-services
                                                            :icon       :workspace_premium
                                                            :icon-color "#449997"
                                                            :label      :services
                                                            :on-click   [:x.router/go-to! "/@app-home/services"]
                                                            :horizontal-weight 1}]
                              [:home.sidebar/add-menu-item! {:group      :products-and-services
                                                             :icon       :workspace_premium
                                                             :icon-color "#449997"
                                                             :label      :services
                                                             :on-click   [:x.router/go-to! "/@app-home/services"]
                                                             :vertical-weight 1}]]}})
