
(ns app.types.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :types.preview
                                                {:collection-name "types"
                                                 :handler-key     :types.preview
                                                 :item-namespace  :type}]})
