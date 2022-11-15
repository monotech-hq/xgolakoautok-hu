
(ns app.rental-vehicles.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :rental-vehicles.viewer
                                              {:base-route      "/@app-home/rental-vehicles"
                                               :collection-name "rental_vehicles"
                                               :handler-key     :rental-vehicles.viewer
                                               :item-namespace  :vehicle
                                               :on-route        [:rental-vehicles.viewer/load!]
                                               :route-title     :rental-vehicles}]})
