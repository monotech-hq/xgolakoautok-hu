
(ns app.clients.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name :business
                                                            :icon       :people
                                                            :icon-color "#5da7d9"
                                                            :label      :clients
                                                            :on-click   [:x.router/go-to! "/@app-home/clients"]
                                                            :horizontal-weight 0}]
                              [:home.sidebar/add-menu-item! {:group-name :business
                                                             :icon       :people
                                                             :icon-color "#5da7d9"
                                                             :label      :clients
                                                             :on-click   [:x.router/go-to! "/@app-home/clients"]
                                                             :vertical-weight 0}]]}})
