
(ns app.packages.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :packages.preview
                                                {:collection-name "packages"
                                                 :handler-key     :packages.preview
                                                 :item-namespace  :package}]})
