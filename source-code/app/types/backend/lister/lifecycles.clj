
(ns app.types.backend.lister.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :types.lister
                                              {:collection-name "types"
                                               :handler-key     :types.lister
                                               :item-namespace  :type}]})
