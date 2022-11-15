
(ns app.vehicle-types.backend.viewer.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :vehicle-types.viewer
                                              {:base-route      "/@app-home/vehicle-models/:model-id/types"
                                               :collection-name "vehicle_types"
                                               :handler-key     :vehicle-types.viewer
                                               :item-namespace  :type
                                               :on-route        [:vehicle-types.viewer/load-viewer!]
                                               :route-title     :vehicle-types}]})
