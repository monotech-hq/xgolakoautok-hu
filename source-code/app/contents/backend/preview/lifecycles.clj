
(ns app.contents.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :contents.preview
                                                {:collection-name "contents"
                                                 :handler-key     :contents.preview
                                                 :item-namespace  :content}]})
