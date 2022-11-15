
(ns app.vehicle-models.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :vehicle-models.lister
                                              {:base-route      "/@app-home/vehicle-models"
                                               :collection-name "vehicle_models"
                                               :handler-key     :vehicle-models.lister
                                               :item-namespace  :model
                                               :on-route        [:vehicle-models.lister/load-lister!]
                                               :route-title     :vehicle-models}]})
