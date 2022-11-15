
(ns app.vehicle-models.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :vehicle-models.editor
                                              {:base-route      "/@app-home/vehicle-models"
                                               :collection-name "vehicle_models"
                                               :handler-key     :vehicle-models.editor
                                               :item-namespace  :model
                                               :on-route        [:vehicle-models.editor/load-editor!]
                                               :route-title     :vehicle-models}]})
