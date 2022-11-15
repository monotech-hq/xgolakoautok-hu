
(ns app.vehicle-models.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name :vehicles
                                                           ;:icon       :view_agenda
                                                            :icon       :view_week
                                                            :icon-color "#4a8bbf"
                                                            :label      :vehicle-models
                                                            :on-click   [:x.router/go-to! "/@app-home/vehicle-models"]
                                                            :horizontal-weight 1}]
                              [:home.sidebar/add-menu-item! {:group-name :vehicles
                                                            ;:icon       :view_agenda
                                                             :icon       :view_week
                                                             :icon-color "#4a79bf"
                                                             :label      :vehicle-models
                                                             :on-click   [:x.router/go-to! "/@app-home/vehicle-models"]
                                                             :vertical-weight 1}]]}})
