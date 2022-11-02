
(ns app.types.backend.editor.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :types.editor
                                              {:base-route      "/@app-home/models/:model-id/types"
                                               :collection-name "types"
                                               :handler-key     :types.editor
                                               :item-namespace  :type
                                               :on-route        [:types.editor/load-editor!]
                                               :route-title     :types}]})
