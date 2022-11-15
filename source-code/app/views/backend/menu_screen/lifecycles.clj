
(ns app.views.backend.menu-screen.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.views/set-menu-screen! [:views.menu-screen/render!]]})
