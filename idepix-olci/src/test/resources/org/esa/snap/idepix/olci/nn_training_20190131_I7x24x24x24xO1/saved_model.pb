Ϝ
��
:
Add
x"T
y"T
z"T"
Ttype:
2	
x
Assign
ref"T�

value"T

output_ref"T�"	
Ttype"
validate_shapebool("
use_lockingbool(�
~
BiasAdd

value"T	
bias"T
output"T" 
Ttype:
2	"-
data_formatstringNHWC:
NHWCNCHW
8
Const
output"dtype"
valuetensor"
dtypetype
.
Identity

input"T
output"T"	
Ttype
N
IsVariableInitialized
ref"dtype�
is_initialized
"
dtypetype�
p
MatMul
a"T
b"T
product"T"
transpose_abool( "
transpose_bbool( "
Ttype:
	2
e
MergeV2Checkpoints
checkpoint_prefixes
destination_prefix"
delete_old_dirsbool(�
=
Mul
x"T
y"T
z"T"
Ttype:
2	�

NoOp
M
Pack
values"T*N
output"T"
Nint(0"	
Ttype"
axisint 
C
Placeholder
output"dtype"
dtypetype"
shapeshape:
~
RandomUniform

shape"T
output"dtype"
seedint "
seed2int "
dtypetype:
2"
Ttype:
2	�
D
Relu
features"T
activations"T"
Ttype:
2	
o
	RestoreV2

prefix
tensor_names
shape_and_slices
tensors2dtypes"
dtypes
list(type)(0�
l
SaveV2

prefix
tensor_names
shape_and_slices
tensors2dtypes"
dtypes
list(type)(0�
H
ShardedFilename
basename	
shard

num_shards
filename
N

StringJoin
inputs*N

output"
Nint(0"
	separatorstring 
:
Sub
x"T
y"T
z"T"
Ttype:
2	
s

VariableV2
ref"dtype�"
shapeshape"
dtypetype"
	containerstring "
shared_namestring �"serve*1.9.02b'v1.9.0-0-g25c197e023'��
h
inputPlaceholder*'
_output_shapes
:���������*
dtype0*
shape:���������
O
outputPlaceholder*
_output_shapes
:*
dtype0*
shape:
q
dense_33_inputPlaceholder*'
_output_shapes
:���������*
dtype0*
shape:���������
n
dense_33/random_uniform/shapeConst*
valueB"      *
_output_shapes
:*
dtype0
`
dense_33/random_uniform/minConst*
valueB
 *�?�*
_output_shapes
: *
dtype0
`
dense_33/random_uniform/maxConst*
valueB
 *�?�>*
_output_shapes
: *
dtype0
�
%dense_33/random_uniform/RandomUniformRandomUniformdense_33/random_uniform/shape*
T0*
_output_shapes

:*
dtype0*
seed2�**
seed���)
}
dense_33/random_uniform/subSubdense_33/random_uniform/maxdense_33/random_uniform/min*
T0*
_output_shapes
: 
�
dense_33/random_uniform/mulMul%dense_33/random_uniform/RandomUniformdense_33/random_uniform/sub*
T0*
_output_shapes

:
�
dense_33/random_uniformAdddense_33/random_uniform/muldense_33/random_uniform/min*
T0*
_output_shapes

:
�
dense_33/kernel
VariableV2*
_output_shapes

:*
dtype0*
shared_name *
	container *
shape
:
�
dense_33/kernel/AssignAssigndense_33/kerneldense_33/random_uniform*
T0*
_output_shapes

:*
use_locking(*
validate_shape(*"
_class
loc:@dense_33/kernel
~
dense_33/kernel/readIdentitydense_33/kernel*
T0*
_output_shapes

:*"
_class
loc:@dense_33/kernel
[
dense_33/ConstConst*
valueB*    *
_output_shapes
:*
dtype0
y
dense_33/bias
VariableV2*
_output_shapes
:*
dtype0*
shared_name *
	container *
shape:
�
dense_33/bias/AssignAssigndense_33/biasdense_33/Const*
T0*
_output_shapes
:*
use_locking(*
validate_shape(* 
_class
loc:@dense_33/bias
t
dense_33/bias/readIdentitydense_33/bias*
T0*
_output_shapes
:* 
_class
loc:@dense_33/bias
�
dense_33/MatMulMatMuldense_33_inputdense_33/kernel/read*
transpose_b( *
T0*
transpose_a( *'
_output_shapes
:���������
�
dense_33/BiasAddBiasAdddense_33/MatMuldense_33/bias/read*
T0*
data_formatNHWC*'
_output_shapes
:���������
Y
dense_33/ReluReludense_33/BiasAdd*
T0*'
_output_shapes
:���������
n
dense_34/random_uniform/shapeConst*
valueB"      *
_output_shapes
:*
dtype0
`
dense_34/random_uniform/minConst*
valueB
 *���*
_output_shapes
: *
dtype0
`
dense_34/random_uniform/maxConst*
valueB
 *��>*
_output_shapes
: *
dtype0
�
%dense_34/random_uniform/RandomUniformRandomUniformdense_34/random_uniform/shape*
T0*
_output_shapes

:*
dtype0*
seed2���*
seed���)
}
dense_34/random_uniform/subSubdense_34/random_uniform/maxdense_34/random_uniform/min*
T0*
_output_shapes
: 
�
dense_34/random_uniform/mulMul%dense_34/random_uniform/RandomUniformdense_34/random_uniform/sub*
T0*
_output_shapes

:
�
dense_34/random_uniformAdddense_34/random_uniform/muldense_34/random_uniform/min*
T0*
_output_shapes

:
�
dense_34/kernel
VariableV2*
_output_shapes

:*
dtype0*
shared_name *
	container *
shape
:
�
dense_34/kernel/AssignAssigndense_34/kerneldense_34/random_uniform*
T0*
_output_shapes

:*
use_locking(*
validate_shape(*"
_class
loc:@dense_34/kernel
~
dense_34/kernel/readIdentitydense_34/kernel*
T0*
_output_shapes

:*"
_class
loc:@dense_34/kernel
[
dense_34/ConstConst*
valueB*    *
_output_shapes
:*
dtype0
y
dense_34/bias
VariableV2*
_output_shapes
:*
dtype0*
shared_name *
	container *
shape:
�
dense_34/bias/AssignAssigndense_34/biasdense_34/Const*
T0*
_output_shapes
:*
use_locking(*
validate_shape(* 
_class
loc:@dense_34/bias
t
dense_34/bias/readIdentitydense_34/bias*
T0*
_output_shapes
:* 
_class
loc:@dense_34/bias
�
dense_34/MatMulMatMuldense_33/Reludense_34/kernel/read*
transpose_b( *
T0*
transpose_a( *'
_output_shapes
:���������
�
dense_34/BiasAddBiasAdddense_34/MatMuldense_34/bias/read*
T0*
data_formatNHWC*'
_output_shapes
:���������
Y
dense_34/ReluReludense_34/BiasAdd*
T0*'
_output_shapes
:���������
n
dense_35/random_uniform/shapeConst*
valueB"      *
_output_shapes
:*
dtype0
`
dense_35/random_uniform/minConst*
valueB
 *���*
_output_shapes
: *
dtype0
`
dense_35/random_uniform/maxConst*
valueB
 *��>*
_output_shapes
: *
dtype0
�
%dense_35/random_uniform/RandomUniformRandomUniformdense_35/random_uniform/shape*
T0*
_output_shapes

:*
dtype0*
seed2��*
seed���)
}
dense_35/random_uniform/subSubdense_35/random_uniform/maxdense_35/random_uniform/min*
T0*
_output_shapes
: 
�
dense_35/random_uniform/mulMul%dense_35/random_uniform/RandomUniformdense_35/random_uniform/sub*
T0*
_output_shapes

:
�
dense_35/random_uniformAdddense_35/random_uniform/muldense_35/random_uniform/min*
T0*
_output_shapes

:
�
dense_35/kernel
VariableV2*
_output_shapes

:*
dtype0*
shared_name *
	container *
shape
:
�
dense_35/kernel/AssignAssigndense_35/kerneldense_35/random_uniform*
T0*
_output_shapes

:*
use_locking(*
validate_shape(*"
_class
loc:@dense_35/kernel
~
dense_35/kernel/readIdentitydense_35/kernel*
T0*
_output_shapes

:*"
_class
loc:@dense_35/kernel
[
dense_35/ConstConst*
valueB*    *
_output_shapes
:*
dtype0
y
dense_35/bias
VariableV2*
_output_shapes
:*
dtype0*
shared_name *
	container *
shape:
�
dense_35/bias/AssignAssigndense_35/biasdense_35/Const*
T0*
_output_shapes
:*
use_locking(*
validate_shape(* 
_class
loc:@dense_35/bias
t
dense_35/bias/readIdentitydense_35/bias*
T0*
_output_shapes
:* 
_class
loc:@dense_35/bias
�
dense_35/MatMulMatMuldense_34/Reludense_35/kernel/read*
transpose_b( *
T0*
transpose_a( *'
_output_shapes
:���������
�
dense_35/BiasAddBiasAdddense_35/MatMuldense_35/bias/read*
T0*
data_formatNHWC*'
_output_shapes
:���������
Y
dense_35/ReluReludense_35/BiasAdd*
T0*'
_output_shapes
:���������
n
dense_36/random_uniform/shapeConst*
valueB"      *
_output_shapes
:*
dtype0
`
dense_36/random_uniform/minConst*
valueB
 *����*
_output_shapes
: *
dtype0
`
dense_36/random_uniform/maxConst*
valueB
 *���>*
_output_shapes
: *
dtype0
�
%dense_36/random_uniform/RandomUniformRandomUniformdense_36/random_uniform/shape*
T0*
_output_shapes

:*
dtype0*
seed2ў�*
seed���)
}
dense_36/random_uniform/subSubdense_36/random_uniform/maxdense_36/random_uniform/min*
T0*
_output_shapes
: 
�
dense_36/random_uniform/mulMul%dense_36/random_uniform/RandomUniformdense_36/random_uniform/sub*
T0*
_output_shapes

:
�
dense_36/random_uniformAdddense_36/random_uniform/muldense_36/random_uniform/min*
T0*
_output_shapes

:
�
dense_36/kernel
VariableV2*
_output_shapes

:*
dtype0*
shared_name *
	container *
shape
:
�
dense_36/kernel/AssignAssigndense_36/kerneldense_36/random_uniform*
T0*
_output_shapes

:*
use_locking(*
validate_shape(*"
_class
loc:@dense_36/kernel
~
dense_36/kernel/readIdentitydense_36/kernel*
T0*
_output_shapes

:*"
_class
loc:@dense_36/kernel
[
dense_36/ConstConst*
valueB*    *
_output_shapes
:*
dtype0
y
dense_36/bias
VariableV2*
_output_shapes
:*
dtype0*
shared_name *
	container *
shape:
�
dense_36/bias/AssignAssigndense_36/biasdense_36/Const*
T0*
_output_shapes
:*
use_locking(*
validate_shape(* 
_class
loc:@dense_36/bias
t
dense_36/bias/readIdentitydense_36/bias*
T0*
_output_shapes
:* 
_class
loc:@dense_36/bias
�
dense_36/MatMulMatMuldense_35/Reludense_36/kernel/read*
transpose_b( *
T0*
transpose_a( *'
_output_shapes
:���������
�
dense_36/BiasAddBiasAdddense_36/MatMuldense_36/bias/read*
T0*
data_formatNHWC*'
_output_shapes
:���������
\
PlaceholderPlaceholder*
_output_shapes

:*
dtype0*
shape
:
�
AssignAssigndense_33/kernelPlaceholder*
T0*
_output_shapes

:*
use_locking( *
validate_shape(*"
_class
loc:@dense_33/kernel
V
Placeholder_1Placeholder*
_output_shapes
:*
dtype0*
shape:
�
Assign_1Assigndense_33/biasPlaceholder_1*
T0*
_output_shapes
:*
use_locking( *
validate_shape(* 
_class
loc:@dense_33/bias
^
Placeholder_2Placeholder*
_output_shapes

:*
dtype0*
shape
:
�
Assign_2Assigndense_34/kernelPlaceholder_2*
T0*
_output_shapes

:*
use_locking( *
validate_shape(*"
_class
loc:@dense_34/kernel
V
Placeholder_3Placeholder*
_output_shapes
:*
dtype0*
shape:
�
Assign_3Assigndense_34/biasPlaceholder_3*
T0*
_output_shapes
:*
use_locking( *
validate_shape(* 
_class
loc:@dense_34/bias
^
Placeholder_4Placeholder*
_output_shapes

:*
dtype0*
shape
:
�
Assign_4Assigndense_35/kernelPlaceholder_4*
T0*
_output_shapes

:*
use_locking( *
validate_shape(*"
_class
loc:@dense_35/kernel
V
Placeholder_5Placeholder*
_output_shapes
:*
dtype0*
shape:
�
Assign_5Assigndense_35/biasPlaceholder_5*
T0*
_output_shapes
:*
use_locking( *
validate_shape(* 
_class
loc:@dense_35/bias
^
Placeholder_6Placeholder*
_output_shapes

:*
dtype0*
shape
:
�
Assign_6Assigndense_36/kernelPlaceholder_6*
T0*
_output_shapes

:*
use_locking( *
validate_shape(*"
_class
loc:@dense_36/kernel
V
Placeholder_7Placeholder*
_output_shapes
:*
dtype0*
shape:
�
Assign_7Assigndense_36/biasPlaceholder_7*
T0*
_output_shapes
:*
use_locking( *
validate_shape(* 
_class
loc:@dense_36/bias
�
IsVariableInitializedIsVariableInitializeddense_33/kernel*
_output_shapes
: *
dtype0*"
_class
loc:@dense_33/kernel
�
IsVariableInitialized_1IsVariableInitializeddense_33/bias*
_output_shapes
: *
dtype0* 
_class
loc:@dense_33/bias
�
IsVariableInitialized_2IsVariableInitializeddense_34/kernel*
_output_shapes
: *
dtype0*"
_class
loc:@dense_34/kernel
�
IsVariableInitialized_3IsVariableInitializeddense_34/bias*
_output_shapes
: *
dtype0* 
_class
loc:@dense_34/bias
�
IsVariableInitialized_4IsVariableInitializeddense_35/kernel*
_output_shapes
: *
dtype0*"
_class
loc:@dense_35/kernel
�
IsVariableInitialized_5IsVariableInitializeddense_35/bias*
_output_shapes
: *
dtype0* 
_class
loc:@dense_35/bias
�
IsVariableInitialized_6IsVariableInitializeddense_36/kernel*
_output_shapes
: *
dtype0*"
_class
loc:@dense_36/kernel
�
IsVariableInitialized_7IsVariableInitializeddense_36/bias*
_output_shapes
: *
dtype0* 
_class
loc:@dense_36/bias
�
initNoOp^dense_33/bias/Assign^dense_33/kernel/Assign^dense_34/bias/Assign^dense_34/kernel/Assign^dense_35/bias/Assign^dense_35/kernel/Assign^dense_36/bias/Assign^dense_36/kernel/Assign
P

save/ConstConst*
valueB Bmodel*
_output_shapes
: *
dtype0
�
save/StringJoin/inputs_1Const*<
value3B1 B+_temp_54e89dcaf8c547cd820d5867fa561a73/part*
_output_shapes
: *
dtype0
u
save/StringJoin
StringJoin
save/Constsave/StringJoin/inputs_1*
	separator *
N*
_output_shapes
: 
Q
save/num_shardsConst*
value	B :*
_output_shapes
: *
dtype0
k
save/ShardedFilename/shardConst"/device:CPU:0*
value	B : *
_output_shapes
: *
dtype0
�
save/ShardedFilenameShardedFilenamesave/StringJoinsave/ShardedFilename/shardsave/num_shards"/device:CPU:0*
_output_shapes
: 
�
save/SaveV2/tensor_namesConst"/device:CPU:0*�
value�B�Bdense_33/biasBdense_33/kernelBdense_34/biasBdense_34/kernelBdense_35/biasBdense_35/kernelBdense_36/biasBdense_36/kernel*
_output_shapes
:*
dtype0
�
save/SaveV2/shape_and_slicesConst"/device:CPU:0*#
valueBB B B B B B B B *
_output_shapes
:*
dtype0
�
save/SaveV2SaveV2save/ShardedFilenamesave/SaveV2/tensor_namessave/SaveV2/shape_and_slicesdense_33/biasdense_33/kerneldense_34/biasdense_34/kerneldense_35/biasdense_35/kerneldense_36/biasdense_36/kernel"/device:CPU:0*
dtypes

2
�
save/control_dependencyIdentitysave/ShardedFilename^save/SaveV2"/device:CPU:0*
T0*
_output_shapes
: *'
_class
loc:@save/ShardedFilename
�
+save/MergeV2Checkpoints/checkpoint_prefixesPacksave/ShardedFilename^save/control_dependency"/device:CPU:0*

axis *
T0*
N*
_output_shapes
:
�
save/MergeV2CheckpointsMergeV2Checkpoints+save/MergeV2Checkpoints/checkpoint_prefixes
save/Const"/device:CPU:0*
delete_old_dirs(
�
save/IdentityIdentity
save/Const^save/MergeV2Checkpoints^save/control_dependency"/device:CPU:0*
T0*
_output_shapes
: 
�
save/RestoreV2/tensor_namesConst"/device:CPU:0*�
value�B�Bdense_33/biasBdense_33/kernelBdense_34/biasBdense_34/kernelBdense_35/biasBdense_35/kernelBdense_36/biasBdense_36/kernel*
_output_shapes
:*
dtype0
�
save/RestoreV2/shape_and_slicesConst"/device:CPU:0*#
valueBB B B B B B B B *
_output_shapes
:*
dtype0
�
save/RestoreV2	RestoreV2
save/Constsave/RestoreV2/tensor_namessave/RestoreV2/shape_and_slices"/device:CPU:0*
dtypes

2*4
_output_shapes"
 ::::::::
�
save/AssignAssigndense_33/biassave/RestoreV2*
T0*
_output_shapes
:*
use_locking(*
validate_shape(* 
_class
loc:@dense_33/bias
�
save/Assign_1Assigndense_33/kernelsave/RestoreV2:1*
T0*
_output_shapes

:*
use_locking(*
validate_shape(*"
_class
loc:@dense_33/kernel
�
save/Assign_2Assigndense_34/biassave/RestoreV2:2*
T0*
_output_shapes
:*
use_locking(*
validate_shape(* 
_class
loc:@dense_34/bias
�
save/Assign_3Assigndense_34/kernelsave/RestoreV2:3*
T0*
_output_shapes

:*
use_locking(*
validate_shape(*"
_class
loc:@dense_34/kernel
�
save/Assign_4Assigndense_35/biassave/RestoreV2:4*
T0*
_output_shapes
:*
use_locking(*
validate_shape(* 
_class
loc:@dense_35/bias
�
save/Assign_5Assigndense_35/kernelsave/RestoreV2:5*
T0*
_output_shapes

:*
use_locking(*
validate_shape(*"
_class
loc:@dense_35/kernel
�
save/Assign_6Assigndense_36/biassave/RestoreV2:6*
T0*
_output_shapes
:*
use_locking(*
validate_shape(* 
_class
loc:@dense_36/bias
�
save/Assign_7Assigndense_36/kernelsave/RestoreV2:7*
T0*
_output_shapes

:*
use_locking(*
validate_shape(*"
_class
loc:@dense_36/kernel
�
save/restore_shardNoOp^save/Assign^save/Assign_1^save/Assign_2^save/Assign_3^save/Assign_4^save/Assign_5^save/Assign_6^save/Assign_7
-
save/restore_allNoOp^save/restore_shard"<
save/Const:0save/Identity:0save/restore_all (5 @F8"�
trainable_variables��
`
dense_33/kernel:0dense_33/kernel/Assigndense_33/kernel/read:02dense_33/random_uniform:08
Q
dense_33/bias:0dense_33/bias/Assigndense_33/bias/read:02dense_33/Const:08
`
dense_34/kernel:0dense_34/kernel/Assigndense_34/kernel/read:02dense_34/random_uniform:08
Q
dense_34/bias:0dense_34/bias/Assigndense_34/bias/read:02dense_34/Const:08
`
dense_35/kernel:0dense_35/kernel/Assigndense_35/kernel/read:02dense_35/random_uniform:08
Q
dense_35/bias:0dense_35/bias/Assigndense_35/bias/read:02dense_35/Const:08
`
dense_36/kernel:0dense_36/kernel/Assigndense_36/kernel/read:02dense_36/random_uniform:08
Q
dense_36/bias:0dense_36/bias/Assigndense_36/bias/read:02dense_36/Const:08"�
	variables��
`
dense_33/kernel:0dense_33/kernel/Assigndense_33/kernel/read:02dense_33/random_uniform:08
Q
dense_33/bias:0dense_33/bias/Assigndense_33/bias/read:02dense_33/Const:08
`
dense_34/kernel:0dense_34/kernel/Assigndense_34/kernel/read:02dense_34/random_uniform:08
Q
dense_34/bias:0dense_34/bias/Assigndense_34/bias/read:02dense_34/Const:08
`
dense_35/kernel:0dense_35/kernel/Assigndense_35/kernel/read:02dense_35/random_uniform:08
Q
dense_35/bias:0dense_35/bias/Assigndense_35/bias/read:02dense_35/Const:08
`
dense_36/kernel:0dense_36/kernel/Assigndense_36/kernel/read:02dense_36/random_uniform:08
Q
dense_36/bias:0dense_36/bias/Assigndense_36/bias/read:02dense_36/Const:08*Z
serving_defaultG
'
input
input:0���������
output
output:0