
(ns app.clients.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :clients.editor
                                              {:base-route      "/@app-home/clients"
                                               :collection-name "clients"
                                               :handler-key     :clients.editor
                                               :item-namespace  :client
                                               :on-route        [:clients.editor/load-editor!]
                                               :route-title     :clients}]})
