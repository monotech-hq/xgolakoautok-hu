
(ns app.home.frontend.sidebar.lifecycles
    (:require [app.home.frontend.sidebar.views :as sidebar.views]
              [x.core.api                      :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-login [:x.ui.sidebar/render-sidebar! :home.sidebar/view
                                            {:content #'sidebar.views/view}]
   :on-logout [:x.ui.sidebar/remove-sidebar! :home.sidebar/view]})
