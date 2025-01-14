// tslint:disable
/**
 * Yugabyte Cloud
 * YugabyteDB as a Service
 *
 * The version of the OpenAPI document: v1
 * Contact: support@yugabyte.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import { useQuery, useInfiniteQuery, useMutation, UseQueryOptions, UseInfiniteQueryOptions, UseMutationOptions } from 'react-query';
import Axios from '../runtime';
import type { AxiosInstance } from 'axios';
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import type {
  ApiError,
  BackupListResponse,
  BackupResponse,
  BackupScheduleSpec,
  BackupSpec,
  RestoreListResponse,
  RestoreResponse,
  RestoreSpec,
  ScheduleListResponse,
  ScheduleResponse,
} from '../models';

export interface CreateBackupForQuery {
  accountId: string;
  projectId: string;
  BackupSpec?: BackupSpec;
}
export interface DeleteBackupForQuery {
  accountId: string;
  projectId: string;
  backupId: string;
}
export interface DeleteClusterBackupsForQuery {
  accountId: string;
  projectId: string;
  clusterId: string;
}
export interface DeleteScheduleForQuery {
  accountId: string;
  projectId: string;
  scheduleId: string;
}
export interface EditBackupScheduleForQuery {
  accountId: string;
  projectId: string;
  scheduleId: string;
  BackupScheduleSpec?: BackupScheduleSpec;
}
export interface GetBackupForQuery {
  accountId: string;
  projectId: string;
  backupId: string;
}
export interface GetScheduleForQuery {
  accountId: string;
  projectId: string;
  scheduleId: string;
}
export interface ListBackupsForQuery {
  accountId: string;
  projectId: string;
  cluster_id?: string;
  action_type?: string;
  state?: string;
  order?: string;
  order_by?: string;
  limit?: number;
  continuation_token?: string;
}
export interface ListRestoresForQuery {
  accountId: string;
  projectId: string;
  backup_id?: string;
  cluster_id?: string;
  state?: string;
  order?: string;
  order_by?: string;
  limit?: number;
  continuation_token?: string;
}
export interface ListSchedulesForQuery {
  accountId: string;
  projectId: string;
  task_type?: string;
  entity_type?: string;
  entity_id?: string;
  state?: string;
  order?: string;
  order_by?: string;
  limit?: number;
  continuation_token?: string;
}
export interface RestoreBackupForQuery {
  accountId: string;
  projectId: string;
  RestoreSpec?: RestoreSpec;
}
export interface ScheduleBackupForQuery {
  accountId: string;
  projectId: string;
  BackupScheduleSpec?: BackupScheduleSpec;
}

/**
 * Create backups
 * Create backups
 */


export const createBackupMutate = (
  body: CreateBackupForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  const url = '/public/accounts/{accountId}/projects/{projectId}/backups'.replace(`{${'accountId'}}`, encodeURIComponent(String(body.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(body.projectId)));
  // eslint-disable-next-line
  // @ts-ignore
  delete body.accountId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.projectId;
  return Axios<BackupResponse>(
    {
      url,
      method: 'POST',
      data: body.BackupSpec
    },
    customAxiosInstance
  );
};

export const useCreateBackupMutation = <Error = ApiError>(
  options?: {
    mutation?:UseMutationOptions<BackupResponse, Error>,
    customAxiosInstance?: AxiosInstance;
  }
) => {
  const {mutation: mutationOptions, customAxiosInstance} = options ?? {};
  // eslint-disable-next-line
  // @ts-ignore
  return useMutation<BackupResponse, Error, CreateBackupForQuery, unknown>((props) => {
    return  createBackupMutate(props, customAxiosInstance);
  }, mutationOptions);
};


/**
 * Delete a backup
 * Delete a backup
 */


export const deleteBackupMutate = (
  body: DeleteBackupForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  const url = '/public/accounts/{accountId}/projects/{projectId}/backups/{backupId}'.replace(`{${'accountId'}}`, encodeURIComponent(String(body.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(body.projectId))).replace(`{${'backupId'}}`, encodeURIComponent(String(body.backupId)));
  // eslint-disable-next-line
  // @ts-ignore
  delete body.accountId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.projectId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.backupId;
  return Axios<unknown>(
    {
      url,
      method: 'DELETE',
    },
    customAxiosInstance
  );
};

export const useDeleteBackupMutation = <Error = ApiError>(
  options?: {
    mutation?:UseMutationOptions<unknown, Error>,
    customAxiosInstance?: AxiosInstance;
  }
) => {
  const {mutation: mutationOptions, customAxiosInstance} = options ?? {};
  // eslint-disable-next-line
  // @ts-ignore
  return useMutation<unknown, Error, DeleteBackupForQuery, unknown>((props) => {
    return  deleteBackupMutate(props, customAxiosInstance);
  }, mutationOptions);
};


/**
 * Delete all the backups of a cluster
 * Submit task to delete all backups of a cluster
 */


export const deleteClusterBackupsMutate = (
  body: DeleteClusterBackupsForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  const url = '/public/accounts/{accountId}/projects/{projectId}/clusters/{clusterId}/backups'.replace(`{${'accountId'}}`, encodeURIComponent(String(body.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(body.projectId))).replace(`{${'clusterId'}}`, encodeURIComponent(String(body.clusterId)));
  // eslint-disable-next-line
  // @ts-ignore
  delete body.accountId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.projectId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.clusterId;
  return Axios<unknown>(
    {
      url,
      method: 'DELETE',
    },
    customAxiosInstance
  );
};

export const useDeleteClusterBackupsMutation = <Error = ApiError>(
  options?: {
    mutation?:UseMutationOptions<unknown, Error>,
    customAxiosInstance?: AxiosInstance;
  }
) => {
  const {mutation: mutationOptions, customAxiosInstance} = options ?? {};
  // eslint-disable-next-line
  // @ts-ignore
  return useMutation<unknown, Error, DeleteClusterBackupsForQuery, unknown>((props) => {
    return  deleteClusterBackupsMutate(props, customAxiosInstance);
  }, mutationOptions);
};


/**
 * Delete a schedule
 * Delete a schedule
 */


export const deleteScheduleMutate = (
  body: DeleteScheduleForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  const url = '/public/accounts/{accountId}/projects/{projectId}/backup_schedules/{scheduleId}'.replace(`{${'accountId'}}`, encodeURIComponent(String(body.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(body.projectId))).replace(`{${'scheduleId'}}`, encodeURIComponent(String(body.scheduleId)));
  // eslint-disable-next-line
  // @ts-ignore
  delete body.accountId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.projectId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.scheduleId;
  return Axios<unknown>(
    {
      url,
      method: 'DELETE',
    },
    customAxiosInstance
  );
};

export const useDeleteScheduleMutation = <Error = ApiError>(
  options?: {
    mutation?:UseMutationOptions<unknown, Error>,
    customAxiosInstance?: AxiosInstance;
  }
) => {
  const {mutation: mutationOptions, customAxiosInstance} = options ?? {};
  // eslint-disable-next-line
  // @ts-ignore
  return useMutation<unknown, Error, DeleteScheduleForQuery, unknown>((props) => {
    return  deleteScheduleMutate(props, customAxiosInstance);
  }, mutationOptions);
};


/**
 * Edit the backup schedule
 * Edit the backup schedule
 */


export const editBackupScheduleMutate = (
  body: EditBackupScheduleForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  const url = '/public/accounts/{accountId}/projects/{projectId}/backup_schedules/{scheduleId}'.replace(`{${'accountId'}}`, encodeURIComponent(String(body.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(body.projectId))).replace(`{${'scheduleId'}}`, encodeURIComponent(String(body.scheduleId)));
  // eslint-disable-next-line
  // @ts-ignore
  delete body.accountId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.projectId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.scheduleId;
  return Axios<ScheduleResponse>(
    {
      url,
      method: 'PUT',
      data: body.BackupScheduleSpec
    },
    customAxiosInstance
  );
};

export const useEditBackupScheduleMutation = <Error = ApiError>(
  options?: {
    mutation?:UseMutationOptions<ScheduleResponse, Error>,
    customAxiosInstance?: AxiosInstance;
  }
) => {
  const {mutation: mutationOptions, customAxiosInstance} = options ?? {};
  // eslint-disable-next-line
  // @ts-ignore
  return useMutation<ScheduleResponse, Error, EditBackupScheduleForQuery, unknown>((props) => {
    return  editBackupScheduleMutate(props, customAxiosInstance);
  }, mutationOptions);
};


/**
 * Get a backup
 * Get a backup
 */

export const getBackupAxiosRequest = (
  requestParameters: GetBackupForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  return Axios<BackupResponse>(
    {
      url: '/public/accounts/{accountId}/projects/{projectId}/backups/{backupId}'.replace(`{${'accountId'}}`, encodeURIComponent(String(requestParameters.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(requestParameters.projectId))).replace(`{${'backupId'}}`, encodeURIComponent(String(requestParameters.backupId))),
      method: 'GET',
      params: {
      }
    },
    customAxiosInstance
  );
};

export const getBackupQueryKey = (
  requestParametersQuery: GetBackupForQuery,
  pageParam = -1,
  version = 1,
) => [
  `/v${version}/public/accounts/{accountId}/projects/{projectId}/backups/{backupId}`,
  pageParam,
  ...(requestParametersQuery ? [requestParametersQuery] : [])
];


export const useGetBackupInfiniteQuery = <T = BackupResponse, Error = ApiError>(
  params: GetBackupForQuery,
  options?: {
    query?: UseInfiniteQueryOptions<BackupResponse, Error, T>;
    customAxiosInstance?: AxiosInstance;
  },
  pageParam = -1,
  version = 1,
) => {
  const queryKey = getBackupQueryKey(params, pageParam, version);
  const { query: queryOptions, customAxiosInstance } = options ?? {};

  const query = useInfiniteQuery<BackupResponse, Error, T>(
    queryKey,
    () => getBackupAxiosRequest(params, customAxiosInstance),
    queryOptions
  );

  return {
    queryKey,
    ...query
  };
};

export const useGetBackupQuery = <T = BackupResponse, Error = ApiError>(
  params: GetBackupForQuery,
  options?: {
    query?: UseQueryOptions<BackupResponse, Error, T>;
    customAxiosInstance?: AxiosInstance;
  },
  version = 1,
) => {
  const queryKey = getBackupQueryKey(params,  version);
  const { query: queryOptions, customAxiosInstance } = options ?? {};

  const query = useQuery<BackupResponse, Error, T>(
    queryKey,
    () => getBackupAxiosRequest(params, customAxiosInstance),
    queryOptions
  );

  return {
    queryKey,
    ...query
  };
};



/**
 * Get a schedule
 * Get a schedule
 */

export const getScheduleAxiosRequest = (
  requestParameters: GetScheduleForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  return Axios<ScheduleResponse>(
    {
      url: '/public/accounts/{accountId}/projects/{projectId}/backup_schedules/{scheduleId}'.replace(`{${'accountId'}}`, encodeURIComponent(String(requestParameters.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(requestParameters.projectId))).replace(`{${'scheduleId'}}`, encodeURIComponent(String(requestParameters.scheduleId))),
      method: 'GET',
      params: {
      }
    },
    customAxiosInstance
  );
};

export const getScheduleQueryKey = (
  requestParametersQuery: GetScheduleForQuery,
  pageParam = -1,
  version = 1,
) => [
  `/v${version}/public/accounts/{accountId}/projects/{projectId}/backup_schedules/{scheduleId}`,
  pageParam,
  ...(requestParametersQuery ? [requestParametersQuery] : [])
];


export const useGetScheduleInfiniteQuery = <T = ScheduleResponse, Error = ApiError>(
  params: GetScheduleForQuery,
  options?: {
    query?: UseInfiniteQueryOptions<ScheduleResponse, Error, T>;
    customAxiosInstance?: AxiosInstance;
  },
  pageParam = -1,
  version = 1,
) => {
  const queryKey = getScheduleQueryKey(params, pageParam, version);
  const { query: queryOptions, customAxiosInstance } = options ?? {};

  const query = useInfiniteQuery<ScheduleResponse, Error, T>(
    queryKey,
    () => getScheduleAxiosRequest(params, customAxiosInstance),
    queryOptions
  );

  return {
    queryKey,
    ...query
  };
};

export const useGetScheduleQuery = <T = ScheduleResponse, Error = ApiError>(
  params: GetScheduleForQuery,
  options?: {
    query?: UseQueryOptions<ScheduleResponse, Error, T>;
    customAxiosInstance?: AxiosInstance;
  },
  version = 1,
) => {
  const queryKey = getScheduleQueryKey(params,  version);
  const { query: queryOptions, customAxiosInstance } = options ?? {};

  const query = useQuery<ScheduleResponse, Error, T>(
    queryKey,
    () => getScheduleAxiosRequest(params, customAxiosInstance),
    queryOptions
  );

  return {
    queryKey,
    ...query
  };
};



/**
 * List backups
 * List backups
 */

export const listBackupsAxiosRequest = (
  requestParameters: ListBackupsForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  return Axios<BackupListResponse>(
    {
      url: '/public/accounts/{accountId}/projects/{projectId}/backups'.replace(`{${'accountId'}}`, encodeURIComponent(String(requestParameters.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(requestParameters.projectId))),
      method: 'GET',
      params: {
        cluster_id: requestParameters['cluster_id'],
        action_type: requestParameters['action_type'],
        state: requestParameters['state'],
        order: requestParameters['order'],
        order_by: requestParameters['order_by'],
        limit: requestParameters['limit'],
        continuation_token: requestParameters['continuation_token'],
      }
    },
    customAxiosInstance
  );
};

export const listBackupsQueryKey = (
  requestParametersQuery: ListBackupsForQuery,
  pageParam = -1,
  version = 1,
) => [
  `/v${version}/public/accounts/{accountId}/projects/{projectId}/backups`,
  pageParam,
  ...(requestParametersQuery ? [requestParametersQuery] : [])
];


export const useListBackupsInfiniteQuery = <T = BackupListResponse, Error = ApiError>(
  params: ListBackupsForQuery,
  options?: {
    query?: UseInfiniteQueryOptions<BackupListResponse, Error, T>;
    customAxiosInstance?: AxiosInstance;
  },
  pageParam = -1,
  version = 1,
) => {
  const queryKey = listBackupsQueryKey(params, pageParam, version);
  const { query: queryOptions, customAxiosInstance } = options ?? {};

  const query = useInfiniteQuery<BackupListResponse, Error, T>(
    queryKey,
    () => listBackupsAxiosRequest(params, customAxiosInstance),
    queryOptions
  );

  return {
    queryKey,
    ...query
  };
};

export const useListBackupsQuery = <T = BackupListResponse, Error = ApiError>(
  params: ListBackupsForQuery,
  options?: {
    query?: UseQueryOptions<BackupListResponse, Error, T>;
    customAxiosInstance?: AxiosInstance;
  },
  version = 1,
) => {
  const queryKey = listBackupsQueryKey(params,  version);
  const { query: queryOptions, customAxiosInstance } = options ?? {};

  const query = useQuery<BackupListResponse, Error, T>(
    queryKey,
    () => listBackupsAxiosRequest(params, customAxiosInstance),
    queryOptions
  );

  return {
    queryKey,
    ...query
  };
};



/**
 * List restore operations
 * List restore operations
 */

export const listRestoresAxiosRequest = (
  requestParameters: ListRestoresForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  return Axios<RestoreListResponse>(
    {
      url: '/public/accounts/{accountId}/projects/{projectId}/restore'.replace(`{${'accountId'}}`, encodeURIComponent(String(requestParameters.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(requestParameters.projectId))),
      method: 'GET',
      params: {
        backup_id: requestParameters['backup_id'],
        cluster_id: requestParameters['cluster_id'],
        state: requestParameters['state'],
        order: requestParameters['order'],
        order_by: requestParameters['order_by'],
        limit: requestParameters['limit'],
        continuation_token: requestParameters['continuation_token'],
      }
    },
    customAxiosInstance
  );
};

export const listRestoresQueryKey = (
  requestParametersQuery: ListRestoresForQuery,
  pageParam = -1,
  version = 1,
) => [
  `/v${version}/public/accounts/{accountId}/projects/{projectId}/restore`,
  pageParam,
  ...(requestParametersQuery ? [requestParametersQuery] : [])
];


export const useListRestoresInfiniteQuery = <T = RestoreListResponse, Error = ApiError>(
  params: ListRestoresForQuery,
  options?: {
    query?: UseInfiniteQueryOptions<RestoreListResponse, Error, T>;
    customAxiosInstance?: AxiosInstance;
  },
  pageParam = -1,
  version = 1,
) => {
  const queryKey = listRestoresQueryKey(params, pageParam, version);
  const { query: queryOptions, customAxiosInstance } = options ?? {};

  const query = useInfiniteQuery<RestoreListResponse, Error, T>(
    queryKey,
    () => listRestoresAxiosRequest(params, customAxiosInstance),
    queryOptions
  );

  return {
    queryKey,
    ...query
  };
};

export const useListRestoresQuery = <T = RestoreListResponse, Error = ApiError>(
  params: ListRestoresForQuery,
  options?: {
    query?: UseQueryOptions<RestoreListResponse, Error, T>;
    customAxiosInstance?: AxiosInstance;
  },
  version = 1,
) => {
  const queryKey = listRestoresQueryKey(params,  version);
  const { query: queryOptions, customAxiosInstance } = options ?? {};

  const query = useQuery<RestoreListResponse, Error, T>(
    queryKey,
    () => listRestoresAxiosRequest(params, customAxiosInstance),
    queryOptions
  );

  return {
    queryKey,
    ...query
  };
};



/**
 * List Schedules
 * List schedules
 */

export const listSchedulesAxiosRequest = (
  requestParameters: ListSchedulesForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  return Axios<ScheduleListResponse>(
    {
      url: '/public/accounts/{accountId}/projects/{projectId}/backup_schedules'.replace(`{${'accountId'}}`, encodeURIComponent(String(requestParameters.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(requestParameters.projectId))),
      method: 'GET',
      params: {
        task_type: requestParameters['task_type'],
        entity_type: requestParameters['entity_type'],
        entity_id: requestParameters['entity_id'],
        state: requestParameters['state'],
        order: requestParameters['order'],
        order_by: requestParameters['order_by'],
        limit: requestParameters['limit'],
        continuation_token: requestParameters['continuation_token'],
      }
    },
    customAxiosInstance
  );
};

export const listSchedulesQueryKey = (
  requestParametersQuery: ListSchedulesForQuery,
  pageParam = -1,
  version = 1,
) => [
  `/v${version}/public/accounts/{accountId}/projects/{projectId}/backup_schedules`,
  pageParam,
  ...(requestParametersQuery ? [requestParametersQuery] : [])
];


export const useListSchedulesInfiniteQuery = <T = ScheduleListResponse, Error = ApiError>(
  params: ListSchedulesForQuery,
  options?: {
    query?: UseInfiniteQueryOptions<ScheduleListResponse, Error, T>;
    customAxiosInstance?: AxiosInstance;
  },
  pageParam = -1,
  version = 1,
) => {
  const queryKey = listSchedulesQueryKey(params, pageParam, version);
  const { query: queryOptions, customAxiosInstance } = options ?? {};

  const query = useInfiniteQuery<ScheduleListResponse, Error, T>(
    queryKey,
    () => listSchedulesAxiosRequest(params, customAxiosInstance),
    queryOptions
  );

  return {
    queryKey,
    ...query
  };
};

export const useListSchedulesQuery = <T = ScheduleListResponse, Error = ApiError>(
  params: ListSchedulesForQuery,
  options?: {
    query?: UseQueryOptions<ScheduleListResponse, Error, T>;
    customAxiosInstance?: AxiosInstance;
  },
  version = 1,
) => {
  const queryKey = listSchedulesQueryKey(params,  version);
  const { query: queryOptions, customAxiosInstance } = options ?? {};

  const query = useQuery<ScheduleListResponse, Error, T>(
    queryKey,
    () => listSchedulesAxiosRequest(params, customAxiosInstance),
    queryOptions
  );

  return {
    queryKey,
    ...query
  };
};



/**
 * Restore a backup to a Cluster
 * Restore a backup to a Cluster
 */


export const restoreBackupMutate = (
  body: RestoreBackupForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  const url = '/public/accounts/{accountId}/projects/{projectId}/restore'.replace(`{${'accountId'}}`, encodeURIComponent(String(body.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(body.projectId)));
  // eslint-disable-next-line
  // @ts-ignore
  delete body.accountId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.projectId;
  return Axios<RestoreResponse>(
    {
      url,
      method: 'POST',
      data: body.RestoreSpec
    },
    customAxiosInstance
  );
};

export const useRestoreBackupMutation = <Error = ApiError>(
  options?: {
    mutation?:UseMutationOptions<RestoreResponse, Error>,
    customAxiosInstance?: AxiosInstance;
  }
) => {
  const {mutation: mutationOptions, customAxiosInstance} = options ?? {};
  // eslint-disable-next-line
  // @ts-ignore
  return useMutation<RestoreResponse, Error, RestoreBackupForQuery, unknown>((props) => {
    return  restoreBackupMutate(props, customAxiosInstance);
  }, mutationOptions);
};


/**
 * Schedule a backup
 * Schedule a backup
 */


export const scheduleBackupMutate = (
  body: ScheduleBackupForQuery,
  customAxiosInstance?: AxiosInstance
) => {
  const url = '/public/accounts/{accountId}/projects/{projectId}/backup_schedules'.replace(`{${'accountId'}}`, encodeURIComponent(String(body.accountId))).replace(`{${'projectId'}}`, encodeURIComponent(String(body.projectId)));
  // eslint-disable-next-line
  // @ts-ignore
  delete body.accountId;
  // eslint-disable-next-line
  // @ts-ignore
  delete body.projectId;
  return Axios<ScheduleResponse>(
    {
      url,
      method: 'POST',
      data: body.BackupScheduleSpec
    },
    customAxiosInstance
  );
};

export const useScheduleBackupMutation = <Error = ApiError>(
  options?: {
    mutation?:UseMutationOptions<ScheduleResponse, Error>,
    customAxiosInstance?: AxiosInstance;
  }
) => {
  const {mutation: mutationOptions, customAxiosInstance} = options ?? {};
  // eslint-disable-next-line
  // @ts-ignore
  return useMutation<ScheduleResponse, Error, ScheduleBackupForQuery, unknown>((props) => {
    return  scheduleBackupMutate(props, customAxiosInstance);
  }, mutationOptions);
};





