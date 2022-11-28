
(ns app.website-pages.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :website-pages.lister
                                              {:base-route      "/@app-home/website-pages"
                                               :collection-name "website_pages"
                                               :handler-key     :website-pages.lister
                                               :item-namespace  :page
                                               :on-route        [:website-pages.lister/load-lister!]
                                               :route-title     :website-pages}]})
