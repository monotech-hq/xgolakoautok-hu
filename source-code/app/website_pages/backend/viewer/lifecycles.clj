
(ns app.website-pages.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :website-pages.viewer
                                              {:base-route      "/@app-home/website-pages"
                                               :collection-name "website_pages"
                                               :handler-key     :website-pages.viewer
                                               :item-namespace  :page
                                               :on-route        [:website-pages.viewer/load-viewer!]
                                               :route-title     :website-pages}]})
