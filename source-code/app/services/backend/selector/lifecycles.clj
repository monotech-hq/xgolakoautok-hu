
(ns app.services.backend.selector.lifecycles
    (:require [engines.item-lister.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :services.selector
                                              {:collection-name "services"
                                               :handler-key     :services.lister
                                               :item-namespace  :service}]})
