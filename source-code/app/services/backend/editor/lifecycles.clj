
(ns app.services.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :services.editor
                                              {:base-route      "/@app-home/services"
                                               :collection-name "services"
                                               :handler-key     :services.editor
                                               :item-namespace  :service
                                               :on-route        [:services.editor/load-editor!]
                                               :route-title     :services}]})
