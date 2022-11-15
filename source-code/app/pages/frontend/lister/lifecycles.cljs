
(ns app.pages.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:disabled?   true
                                                            :group-name  :content
                                                            :icon        :description
                                                            :icon-color  "#5564b1"
                                                            :icon-family :material-icons-outlined
                                                            :label       :pages
                                                            :on-click    [:x.router/go-to! "/@app-home/pages"]
                                                            :horizontal-weight 0}]
                              [:home.sidebar/add-menu-item! {:disabled?   true
                                                             :group-name  :content
                                                             :icon        :description
                                                             :icon-color  "#5c72e5"
                                                             :icon-family :material-icons-outlined
                                                             :label       :pages
                                                             :on-click    [:x.router/go-to! "/@app-home/pages"]
                                                             :vertical-weight 0}]]}})
