
(ns app.models.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :models.editor
                                              {:base-route      "/@app-home/models"
                                               :collection-name "models"
                                               :handler-key     :models.editor
                                               :item-namespace  :model
                                               :on-route        [:models.editor/load-editor!]
                                               :route-title     :models}]})
