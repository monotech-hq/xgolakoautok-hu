
(ns app.clients.backend.selector.lifecycles
    (:require [engines.item-lister.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :clients.selector
                                              {:collection-name "clients"
                                               :handler-key     :clients.lister
                                               :item-namespace  :client}]})
