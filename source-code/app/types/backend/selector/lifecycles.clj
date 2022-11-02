
(ns app.types.backend.selector.lifecycles
    (:require [engines.item-lister.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :types.selector
                                              {:collection-name "types"
                                               :handler-key     :types.lister
                                               :item-namespace  :type}]})
