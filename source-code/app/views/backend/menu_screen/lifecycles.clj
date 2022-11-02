
(ns app.views.backend.menu-screen.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:views/set-menu-screen! [:views.menu-screen/render!]]})
