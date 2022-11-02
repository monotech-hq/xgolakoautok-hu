
(ns site.effects.backend.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:environment/add-css! {:uri "/site/css/effects.css"}]})
