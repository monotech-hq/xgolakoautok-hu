
(ns app.rental-vehicles.frontend.lister.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name  :vehicles
                                                            :icon        :airport_shuttle
                                                            :icon-color  "#4a8bbf"
                                                            :icon-family :material-icons-outlined
                                                            :label       :rental-vehicles
                                                            :on-click    [:x.router/go-to! "/@app-home/rental-vehicles"]
                                                            :horizontal-weight 0}]
                              [:home.sidebar/add-menu-item! {:group-name  :vehicles
                                                             :icon        :airport_shuttle
                                                             :icon-color  "#4a79bf"
                                                             :icon-family :material-icons-outlined
                                                             :label       :rental-vehicles
                                                             :on-click    [:x.router/go-to! "/@app-home/rental-vehicles"]
                                                             :vertical-weight 0}]]}})
