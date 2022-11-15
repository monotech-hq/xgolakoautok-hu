
(ns app.clients.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :clients.preview
                                                {:collection-name "clients"
                                                 :handler-key     :clients.preview
                                                 :item-namespace  :client}]})
