
(ns app.services.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :services.preview
                                                {:collection-name "services"
                                                 :handler-key     :services.preview
                                                 :item-namespace  :service}]})
