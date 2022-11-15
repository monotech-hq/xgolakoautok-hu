
(ns app.views.backend.error-screen.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.views/set-error-screen! [:views.error-screen/render!]]})
