
(ns app.clients.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group      :business
                                                            :icon       :people
                                                            :icon-color "#805dd9"
                                                            :label      :clients
                                                            :on-click   [:x.router/go-to! "/@app-home/clients"]
                                                            :horizontal-weight 3}]
                              [:home.sidebar/add-menu-item! {:group      :business
                                                             :icon       :people
                                                             :icon-color "#805dd9"
                                                             :label      :clients
                                                             :on-click   [:x.router/go-to! "/@app-home/clients"]
                                                             :vertical-weight 3}]]}})
