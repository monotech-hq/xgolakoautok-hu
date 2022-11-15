
(ns app.vehicle-types.backend.lister.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :vehicle-types.lister
                                              {:collection-name "vehicle_types"
                                               :handler-key     :vehicle-types.lister
                                               :item-namespace  :type}]})
