
(ns app.user.backend.login-screen.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.views/set-login-screen! [:user.login-screen/render!]]})
