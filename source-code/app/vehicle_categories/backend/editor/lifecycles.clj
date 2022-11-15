
(ns app.vehicle-categories.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :vehicle-categories.editor
                                              {:base-route      "/@app-home/vehicle-categories"
                                               :collection-name "vehicle_categories"
                                               :handler-key     :vehicle-categories.editor
                                               :item-namespace  :category
                                               :on-route        [:vehicle-categories.editor/load-editor!]
                                               :route-title     :vehicle-categories}]})
