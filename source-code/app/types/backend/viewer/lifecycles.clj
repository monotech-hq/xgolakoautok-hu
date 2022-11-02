
(ns app.types.backend.viewer.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :types.viewer
                                              {:base-route      "/@app-home/models/:model-id/types"
                                               :collection-name "types"
                                               :handler-key     :types.viewer
                                               :item-namespace  :type
                                               :on-route        [:types.viewer/load-viewer!]
                                               :route-title     :types}]})
