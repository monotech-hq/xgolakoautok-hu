
(ns app.vehicle-categories.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name :vehicles
                                                           ;:icon       :view_list
                                                            :icon       :view_agenda
                                                            :icon-color "#4a8bbf"
                                                            :label      :vehicle-categories
                                                            :on-click   [:x.router/go-to! "/@app-home/vehicle-categories"]
                                                            :horizontal-weight 0}]
                              [:home.sidebar/add-menu-item! {:group-name :vehicles
                                                            ;:icon       :view_list
                                                             :icon       :view_agenda
                                                             :icon-color "#4a79bf"
                                                             :label      :vehicle-categories
                                                             :on-click   [:x.router/go-to! "/@app-home/vehicle-categories"]
                                                             :vertical-weight 0}]]}})
