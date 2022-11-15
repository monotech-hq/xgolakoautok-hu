
(ns app.contents.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :contents.viewer
                                              {:base-route      "/@app-home/contents"
                                               :collection-name "contents"
                                               :handler-key     :contents.viewer
                                               :item-namespace  :content
                                               :on-route        [:contents.viewer/load-viewer!]
                                               :route-title     :contents}]})
