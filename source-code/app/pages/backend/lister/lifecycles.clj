
(ns app.pages.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :pages.lister
                                              {:base-route      "/@app-home/pages"
                                               :collection-name "pages"
                                               :handler-key     :pages.lister
                                               :item-namespace  :page
                                               :on-route        [:pages.lister/load-lister!]
                                               :route-title     :pages}]})
