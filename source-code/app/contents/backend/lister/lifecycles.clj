
(ns app.contents.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :contents.lister
                                              {:base-route      "/@app-home/contents"
                                               :collection-name "contents"
                                               :handler-key     :contents.lister
                                               :item-namespace  :content
                                               :on-route        [:contents.lister/load-lister!]
                                               :route-title     :contents}]})
