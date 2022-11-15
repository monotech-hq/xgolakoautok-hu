
(ns app.rental-vehicles.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :rental-vehicles.lister
                                              {:base-route      "/@app-home/rental-vehicles"
                                               :collection-name "rental_vehicles"
                                               :handler-key     :rental-vehicles.lister
                                               :item-namespace  :vehicle
                                               :on-route        [:rental-vehicles.lister/load!]
                                               :route-title     :rental-vehicles}]})
