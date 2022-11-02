
(ns app.packages.backend.selector.lifecycles
    (:require [engines.item-lister.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :packages.selector
                                              {:collection-name "packages"
                                               :handler-key     :packages.lister
                                               :item-namespace  :package}]})
