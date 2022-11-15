
(ns app.models.backend.selector.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :models.selector
                                              {:collection-name "models"
                                               :handler-key     :models.lister
                                               :item-namespace  :model}]})
