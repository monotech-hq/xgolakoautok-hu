
(ns app.home.frontend.sidebar.lifecycles
    (:require [app.home.frontend.sidebar.views :as sidebar.views]
              [x.app-core.api                  :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-login [:ui.sidebar/render-sidebar! :home.sidebar/view
                                          {:content #'sidebar.views/view}]
   :on-logout [:ui.sidebar/remove-sidebar! :home.sidebar/view]})
